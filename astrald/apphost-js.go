package astral

import (
	"encoding/json"
	"fmt"
	"github.com/cryptopunkscc/astrald/lib/astral"
	"github.com/cryptopunkscc/go-astral-js"
)

type JsAppHostClient struct {
	adapter *astraljs.AppHostFlatAdapter
}

func NewJsAppHostClient() *JsAppHostClient {
	return &JsAppHostClient{adapter: astraljs.NewAppHostFlatAdapter()}
}

func (client *JsAppHostClient) Close() {
	astraljs.CloseAppHostFlatAdapter(client.adapter)
}

func (client *JsAppHostClient) Log(arg string) {
	client.adapter.Log(arg)
}

func (client *JsAppHostClient) LogArr(arg string) {
	var stringSlice []string
	err := json.Unmarshal([]byte(arg), &stringSlice)
	if err != nil {
		fmt.Printf("cannot parse '%v' as JSON array: %v\n", arg, err)
		return
	}
	arr := make([]any, len(stringSlice))
	for i, v := range stringSlice {
		arr[i] = v
	}
	client.adapter.LogArr(arr)
}

func (client *JsAppHostClient) Sleep(duration int64) {
	client.adapter.Sleep(duration)
}

func (client *JsAppHostClient) ServiceRegister(service string) (err error) {
	return client.adapter.ServiceRegister(service)
}

func (client *JsAppHostClient) ServiceClose(service string) (err error) {
	return client.adapter.ServiceClose(service)
}

func (client *JsAppHostClient) ConnAccept(service string) (id string, err error) {
	return client.adapter.ConnAccept(service)
}

func (client *JsAppHostClient) ConnClose(id string) (err error) {
	return client.adapter.ConnClose(id)
}

func (client *JsAppHostClient) ConnWrite(id string, data string) (err error) {
	return client.adapter.ConnWrite(id, data)
}

func (client *JsAppHostClient) ConnRead(id string) (data string, err error) {
	return client.adapter.ConnRead(id)
}

func (client *JsAppHostClient) Query(identity string, query string) (connId string, err error) {
	return client.adapter.Query(identity, query)
}

func (client *JsAppHostClient) QueryName(name string, query string) (connId string, err error) {
	return client.adapter.QueryName(name, query)
}

func (client *JsAppHostClient) Resolve(name string) (id string, err error) {
	return client.adapter.Resolve(name)
}

func (client *JsAppHostClient) GetNodeInfo(identity string) (info *NodeInfo, err error) {
	i, err := client.adapter.NodeInfo(identity)
	if err == nil {
		ni := NodeInfo(i)
		info = &ni
	}
	return
}

type NodeInfo astraljs.NodeInfo

func init() {
	astral.Client = *astral.NewClient("memu:apphost", "")
}
