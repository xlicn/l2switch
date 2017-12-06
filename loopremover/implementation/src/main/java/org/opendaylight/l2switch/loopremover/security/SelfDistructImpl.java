/*
 * Copyright (c) 2017 lixing and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.l2switch.loopremover.security;


import com.google.common.util.concurrent.Futures;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.loop.remover.config.rev140528.LoopRemoverConfigService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.loop.remover.config.rev140528.NetworkDestructBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.loop.remover.config.rev140528.SelfDestructOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.loop.remover.config.rev140528.SelfDestructOutputBuilder;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

import java.util.concurrent.Future;

public class SelfDistructImpl implements LoopRemoverConfigService{
    private NotificationPublishService notificationPublishService;

    public SelfDistructImpl(NotificationPublishService notificationPublishService){
        this.notificationPublishService = notificationPublishService;
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

        rpcResultBuilder.withResult(selfDestructOutputBuilder.build());

        return Futures.immediateFuture(rpcResultBuilder.build());
    }



}
