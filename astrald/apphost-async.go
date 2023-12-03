package astral

type AsyncAppHostClient interface {
	Resolve(name string, callback StringCallback)
	Register(name string, callback ApphostListenerCallback)
	Query(nodeID string, query string, callback ConnCallback)
}

type AsyncApphostListener interface {
	Next(callback QueryDataCallback)
}

type AsyncQueryData interface {
	Caller() string
	Accept(callback ConnCallback)
	Reject(callback Callback)
	Query() string
}

type AsyncConn interface {
	Read(p []byte, callback IntCallback)
	ReadN(n int, callback BytesCallback)
	Write(p []byte, callback IntCallback)
	Close(callback Callback)
}

type StringCallback CallbackR[string]
type ApphostListenerCallback CallbackR[AsyncApphostListener]
type ConnCallback CallbackR[AsyncConn]
type QueryDataCallback CallbackR[QueryData]
type BytesCallback CallbackR[[]byte]
type IntCallback CallbackR[int]

type CallbackR[R any] interface {
	Call(value R, err string)
}

type Callback interface {
	Call(err string)
}
