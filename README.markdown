# Fault Injection
There are two faults we can used for experiments.
> * [Bug 5816 - l2switch - Expired hosts never comeback after timing out (though ping works)][1]
> * The injected self-destruction fault

---
## Fault 1

In reactive mode(configure in *arp-handler-config*), when we use host-expiry feature(configure in *host-tracker-config*), expired hosts never comeback even ping works.

**Root cause:**
> Mutual influence of flow idletimeout/hardtimeout values in l2switch-main module, timestamp-update-interval value in address-tracker module, and host-purge-age value in host-tracker module

**Reproduct with this code:**
1. Start controller and mininet with any topology
2. Pingall and check the topo descovery in controller
3. After few seconds(default 30s) and check topo in controller
4. Do another pingall operation

## Fault 2

**The injection architecture:**
![architecture][2]

**Root cause:**
> Data conflict of self-destruct-switch in datastore caused by external restful request disorder.

**Reproduct with this code:**
Driectly send self-destruct request without send set-secure-state request first


[1]: https://bugs.opendaylight.org/show_bug.cgi?id=5816
[2]: injection.PNG