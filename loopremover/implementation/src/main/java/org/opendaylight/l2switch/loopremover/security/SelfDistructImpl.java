/*
 * Copyright (c) 2017 lixing and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.l2switch.loopremover.security;


import com.google.common.base.Optional;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.Futures;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.loop.remover.config.rev140528.*;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

import java.util.concurrent.Future;

public class SelfDistructImpl implements LoopRemoverConfigService{
    private NotificationPublishService notificationPublishService;
    private DataBroker dataBroker;

    public SelfDistructImpl(NotificationPublishService notificationPublishService, DataBroker dataBroker){
        this.notificationPublishService = notificationPublishService;
        this.dataBroker = dataBroker;
    }

    @Override
    public Future<RpcResult<SelfDestructOutput>> selfDestruct(){
        RpcResultBuilder<SelfDestructOutput> rpcResultBuilder = null;
        rpcResultBuilder = RpcResultBuilder.failed();

        SelfDestructOutputBuilder selfDestructOutputBuilder = new SelfDestructOutputBuilder();

        NetworkDestructBuilder networkDestructBuilder = new NetworkDestructBuilder();
        networkDestructBuilder.setDropAll(true);
        try {
            notificationPublishService.putNotification(networkDestructBuilder.build());
            selfDestructOutputBuilder.setResult("Success to publish network destruct notification.");
            rpcResultBuilder = RpcResultBuilder.success();
        }catch (InterruptedException ie){
            selfDestructOutputBuilder.setResult("Woops, fail to publish network destruct notification.");
        }

        ReadOnlyTransaction readOnlyTransaction = dataBroker.newReadOnlyTransaction();
        InstanceIdentifier<LoopRemoverConfig> id = InstanceIdentifier.builder(LoopRemoverConfig.class).build();
        CheckedFuture<Optional<LoopRemoverConfig>, ReadFailedException> checkedFuture = readOnlyTransaction.read(LogicalDatastoreType.CONFIGURATION,id);

        try {
            //Optional<SelfDestructSwitch> optional = readOnlyTransaction.read(LogicalDatastoreType.CONFIGURATION,id).get();
            Optional<LoopRemoverConfig> optional = checkedFuture.checkedGet();
            if (optional.isPresent()){
                System.out.println("good, get it");

            }else {
                System.out.println("not get, but in");
            }
        } catch (ReadFailedException e) {
            e.printStackTrace();
            System.out.println("Fail to read security level");
        }


        rpcResultBuilder.withResult(selfDestructOutputBuilder.build());

        return Futures.immediateFuture(rpcResultBuilder.build());
    }



}
