-- 操作可用储蓄卡,当客户购买则减,卖家增加可用库存则加，使用lua脚本redis操作的保证原子性
redis.call('INCRBY', KEYS[1], ARGV[1])
local current = redis.call('GET', KEYS[1])
local result = {}
result["@class"] = "org.meibaobao.ecos.basecomponent.common.Result"
if tonumber(current) < 0 then
    redis.call('DECRBY', KEYS[1], ARGV[1])
    current = redis.call('GET', KEYS[1])
    result["success"] = false
    result["data"] = current
    local encodestr = cjson.encode(result)
    print(encodestr)
    return encodestr
end
result["success"] = true
result["data"] = current
local encodestr = cjson.encode(result)
print(encodestr)
return encodestr