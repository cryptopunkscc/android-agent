package astral

import (
	"encoding/json"
	"fmt"
	"github.com/cryptopunkscc/astrald/lib/astral"
	"github.com/cryptopunkscc/go-astral-js"
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

func (client *AppHostClient) LogArr(arg string) {
	var stringSlice []string
	err := json.Unmarshal([]byte(arg), &stringSlice)
	if err != nil {
		fmt.Println("Error parsing JSON array:", err)
	}
	arr := make([]any, len(stringSlice))
	for i, v := range stringSlice {
		arr[i] = v
	}
	client.adapter.LogArr(arr)
}

func (client *AppHostClient) Sleep(duration int64) {
	client.adapter.Sleep(duration)
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

func init() {
	astral.Client = *astral.NewClient("tcp:127.0.0.1:8625", "")
}
