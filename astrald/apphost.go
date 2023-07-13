package astral

import (
	"github.com/cryptopunkscc/android-astral-agent/astraljs"
)

type AppHostClient struct {
	adapter *astraljs.AppHostFlatAdapter
}

func NewAppHostClient() *AppHostClient {
	return &AppHostClient{adapter: astraljs.NewAppHostFlatAdapter()}
}

func (client *AppHostClient) Log(arg string) {
	client.adapter.Log(arg)
}

func (client *AppHostClient) LogArr(arg []string) {
	arr := make([]any, len(arg))
	for i, v := range arg {
		arr[i] = v
	}
	client.adapter.LogArr(arr)
}

func (client *AppHostClient) Sleep(duration int64) {
	client.adapter.Sleep(duration)
}

func (client *AppHostClient) ServiceExec(identity string, service string) (err error) {
	return client.adapter.ServiceExec(identity, service, nil, nil)
}

func (client *AppHostClient) ServiceRegister(service string) (err error) {
	return client.adapter.ServiceRegister(service)
}

func (client *AppHostClient) ServiceClose(service string) (err error) {
	return client.adapter.ServiceClose(service)
}

func (client *AppHostClient) ConnAccept(service string) (id string, err error) {
	return client.adapter.ConnAccept(service)
}

func (client *AppHostClient) ConnClose(id string) (err error) {
	return client.adapter.ConnClose(id)
}

func (client *AppHostClient) ConnWrite(id string, data string) (err error) {
	return client.adapter.ConnWrite(id, data)
}

func (client *AppHostClient) ConnRead(id string) (data string, err error) {
	return client.adapter.ConnRead(id)
}

func (client *AppHostClient) Query(identity string, query string) (connId string, err error) {
	return client.adapter.Query(identity, query)
}

func (client *AppHostClient) QueryName(name string, query string) (connId string, err error) {
	return client.adapter.QueryName(name, query)
}

func (client *AppHostClient) Resolve(name string) (id string, err error) {
	return client.adapter.Resolve(name)
}

func (client *AppHostClient) GetNodeInfo(identity string) (info *NodeInfo, err error) {
	i, err := client.adapter.NodeInfo(identity)
	if err == nil {
		ni := NodeInfo(i)
		info = &ni
	}
	return
}

type NodeInfo astraljs.NodeInfo
