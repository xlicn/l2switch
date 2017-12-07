/*
 * Copyright (c) 2017 lixing and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.l2switch.security;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.CheckedFuture;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.loop.remover.config.rev140528.LoopRemoverConfigListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.loop.remover.config.rev140528.NetworkDestruct;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.address.tracker.config.rev160621.SelfDestructSwitch;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SelfDestructHandler implements LoopRemoverConfigListener{
    private static final Logger LOG = LoggerFactory.getLogger(SelfDestructHandler.class);
    private DataBroker dataBroker;

    public SelfDestructHandler(DataBroker dataBroker){
        this.dataBroker = dataBroker;
    }


    @Override
    public void onNetworkDestruct(NetworkDestruct notification){
        System.out.println("l2switch-main receive the notification");
        if (notification.isDropAll()){

            ReadOnlyTransaction readOnlyTransaction = dataBroker.newReadOnlyTransaction();
            InstanceIdentifier<SelfDestructSwitch> id = InstanceIdentifier.builder(SelfDestructSwitch.class).build();
            CheckedFuture<Optional<SelfDestructSwitch>, ReadFailedException> checkedFuture = readOnlyTransaction.read(LogicalDatastoreType.CONFIGURATION,id);

            try {
                //Optional<SelfDestructSwitch> optional = readOnlyTransaction.read(LogicalDatastoreType.CONFIGURATION,id).get();
                Optional<SelfDestructSwitch> optional = checkedFuture.checkedGet();
                if (optional.isPresent()){
                    System.out.println("Security level is "+optional.get().isSwitch());
                    if (optional.get().isSwitch()){
                        LOG.debug("Start to self destruct...goodbye.");
                    }else {
                        LOG.debug("We can still make it.");
                    }
                }
            } catch (ReadFailedException e) {
                e.printStackTrace();
                LOG.debug("Fail to read security level");
            }
        }else {
            LOG.debug("Error: bad notification");
        }
    }
}
