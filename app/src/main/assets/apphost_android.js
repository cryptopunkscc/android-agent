function astral_conn_accept(arg1) {
  return _app_host.connAccept(arg1);
}

function astral_conn_close(arg1) {
  return _app_host.connClose(arg1);
}

function astral_conn_read(arg1) {
  return _app_host.connRead(arg1);
}

function astral_conn_write(arg1, arg2) {
  return _app_host.connWrite(arg1, arg2);
}

function astral_node_info(arg1) {
  return JSON.parse(_app_host.nodeInfo(arg1));
}

function astral_query(arg1, arg2) {
  return _app_host.query(arg1, arg2);
}

function astral_query_name(arg1, arg2) {
  return _app_host.queryName(arg1, arg2);
}

function astral_resolve(arg1) {
  return _app_host.resolve(arg1);
}

function astral_service_close(arg1) {
  return _app_host.serviceClose(arg1);
}

function astral_service_register(arg1) {
  return _app_host.serviceRegister(arg1);
}

function log(...arg1) {
  return _app_host.logArr(arg1);
}

function sleep(arg1) {
  return _app_host.sleep(arg1);
}
