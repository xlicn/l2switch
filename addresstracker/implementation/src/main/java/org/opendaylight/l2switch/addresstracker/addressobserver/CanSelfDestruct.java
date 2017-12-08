/*
 * Copyright (c) 2017 lixing and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.l2switch.addresstracker.addressobserver;


import org.opendaylight.controller.md.sal.binding.api.*;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.address.tracker.config.rev160621.SelfDestructSwitch;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.address.tracker.config.rev160621.SelfDestructSwitchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.arp.handler.config.rev140528.SecureState;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Collection;


public class CanSelfDestruct implements DataTreeChangeListener<SecureState> {
    private static final Logger LOG = LoggerFactory.getLogger(CanSelfDestruct.class);

    private final DataBroker dataBroker;
    private InstanceIdentifier<SecureState> identifier = InstanceIdentifier.create(SecureState.class);


    public CanSelfDestruct(DataBroker dataBroker){
        this.dataBroker = dataBroker;
    }
    public ListenerRegistration<CanSelfDestruct> register(DataBroker dataBroker){
        return dataBroker.registerDataTreeChangeListener(new DataTreeIdentifier<SecureState>(LogicalDatastoreType.CONFIGURATION,identifier),this);
    }

    @Override
    public void onDataTreeChanged(@Nonnull Collection<DataTreeModification<SecureState>> changes) {
        System.out.println("###address-tracker hears Secure-state data tree modification###");
        for(DataTreeModification<SecureState> change :changes){
            SecureState dataAfter =change.getRootNode().getDataAfter();
            ReadWriteTransaction readWriteTransaction = dataBroker.newReadWriteTransaction();
            InstanceIdentifier<SelfDestructSwitch> id = InstanceIdentifier.builder(SelfDestructSwitch.class).build();
            SelfDestructSwitchBuilder selfDestructSwitchBuilder = new SelfDestructSwitchBuilder();

            if(dataAfter.getLevel()==2){
                selfDestructSwitchBuilder.setSwitch(true);
            }else {
                selfDestructSwitchBuilder.setSwitch(false);
            }
            readWriteTransaction.put(LogicalDatastoreType.CONFIGURATION, id, selfDestructSwitchBuilder.build());
            try {
                readWriteTransaction.submit().checkedGet();
                System.out.println("The security level is "+dataAfter.getLevel());
                System.out.println("Set can-self-destruct-switch to "+selfDestructSwitchBuilder.isSwitch());
            }catch (TransactionCommitFailedException tcfe){
                System.out.println("Woops, fail to set can-self-destruct-switch");
            }
        }
    }
}
