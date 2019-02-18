# distributed-lock
distributed lock and rate limit using redis

使用lua脚本和Redis实现了锁和限流,Redis是单机实现，使用集群的时候需要改造。Reids自己也实现了RedLock,java实现版本为Redission,
有很多公司使用了，功能非常强大。


`lock.lua`脚本执行了Redis事务锁`set <key> <value> nx px <millis>`命令,可以改为`set <key> <value> nx ex <seconds>`


