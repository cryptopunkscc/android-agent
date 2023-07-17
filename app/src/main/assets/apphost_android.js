const _awaiting = new Map()

function _promise(block) {
    return new Promise((resolve, reject) => {
        this.id = block()
        _awaiting.set(this.id, [resolve, reject])
    });
}

function _resolve(id, value) {
    _awaiting.get(id)[0](value);
    _awaiting.delete(id);
}

function _reject(id, error) {
    _awaiting.get(id)[1](error);
    _awaiting.delete(id);
}

function astral_conn_accept(arg1) {
    return _promise(() => {
        return _app_host.connAccept(arg1);
    });
}

function astral_conn_close(arg1) {
    return _promise(() => {
        return _app_host.connClose(arg1);
    });
}

function astral_conn_read(arg1) {
    return _promise(() => {
        return _app_host.connRead(arg1);
    });
}

function astral_conn_write(arg1, arg2) {
    return _promise(() => {
        return _app_host.connWrite(arg1, arg2);
    });
}

function astral_node_info(arg1) {
    return _promise(() => {
        return _app_host.nodeInfo(arg1);
    }).then(v => {
        return JSON.parse(v);
    });
}

function astral_query(arg1, arg2) {
    return _promise(() => {
        return _app_host.query(arg1, arg2);
    });
}

function astral_query_name(arg1, arg2) {
    return _promise(() => {
        return _app_host.queryName(arg1, arg2);
    });
}

function astral_resolve(arg1) {
    return _promise(() => {
        return _app_host.resolve(arg1);
    });
}

function astral_service_close(arg1) {
    return _promise(() => {
        return _app_host.serviceClose(arg1);
    });
}

function astral_service_register(arg1) {
    return _promise(() => {
        return _app_host.serviceRegister(arg1);
    });
}

function log(...arg1) {
    return _app_host.logArr(JSON.stringify(arg1));
}

function sleep(arg1) {
    return _promise(() => {
        return _app_host.sleep(arg1);
    });
}
