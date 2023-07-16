async function astral_conn_accept(arg1) {
  return await _app_host.connAccept(arg1);
}

async function astral_conn_close(arg1) {
  return await _app_host.connClose(arg1);
}

async function astral_conn_read(arg1) {
  return await _app_host.connRead(arg1);
}

async function astral_conn_write(arg1, arg2) {
  return await _app_host.connWrite(arg1, arg2);
}

async function astral_node_info(arg1) {
  return await JSON.parse(_app_host.nodeInfo(arg1));
}

async function astral_query(arg1, arg2) {
  return await _app_host.query(arg1, arg2);
}

async function astral_query_name(arg1, arg2) {
  return await _app_host.queryName(arg1, arg2);
}

async function astral_resolve(arg1) {
  return await _app_host.resolve(arg1);
}

async function astral_service_close(arg1) {
  return await _app_host.serviceClose(arg1);
}

async function astral_service_register(arg1) {
  return await _app_host.serviceRegister(arg1);
}

async function log(...arg1) {
  return await _app_host.logArr(JSON.stringify(arg1));
}

async function sleep(arg1) {
  return await _app_host.sleep(arg1);
}
