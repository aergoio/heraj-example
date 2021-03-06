function constructor(key, arg1, arg2)
  if key ~= nil then
    system.setItem(key, {intVal=arg1, stringVal=arg2})
  end
end

function set(key, arg1, arg2)
  contract.event("set", key, arg1, arg2)
  system.setItem(key, {intVal=arg1, stringVal=arg2})
end

function get(key)
  return system.getItem(key)
end

function check_delegation()
  return true
end

abi.register_view(get)
abi.register(set)
abi.fee_delegation(set)
abi.payable(set)
