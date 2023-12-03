package astral

import (
	"github.com/cryptopunkscc/astrald/auth/id"
	"github.com/cryptopunkscc/astrald/lib/astral"
	"io"
)

type AppHostClient interface {
	Resolve(name string) (s string, err error)
	Register(name string) (ApphostListener, error)
	Query(nodeID string, query string) (c Conn, err error)
}

type ApphostListener interface {
	Next() (query QueryData, err error)
}

type QueryData interface {
	Caller() string
	Accept() (c Conn, err error)
	Reject() error
	Query() string
}

type Conn interface {
	Read(p []byte) (n int, err error)
	ReadN(n int) (arr []byte, err error)
	Write(p []byte) (n int, err error)
	Close() error
}

func NewApphostClient() AppHostClient {
	return &appHostClient{}
}

type appHostClient struct{}

func (a *appHostClient) Resolve(name string) (s string, err error) {
	var i id.Identity
	i, err = astral.Resolve(name)
	if err != nil {
		return
	}
	s = i.String()
	return
}

func (a *appHostClient) Register(name string) (ApphostListener, error) {
	listener, err := astral.Register(name)
	if err != nil {
		return nil, err
	}
	return &apphostListener{listener}, err
}

func (a *appHostClient) Query(nodeID string, query string) (c Conn, err error) {
	i, err := id.ParsePublicKeyHex(nodeID)
	if err != nil {
		return
	}
	cc := &conn{}
	cc.Conn, err = astral.Query(i, query)
	c = cc
	return
}

var _ io.ReadWriteCloser = &conn{}

type conn struct {
	Conn io.ReadWriteCloser
}

func (c *conn) ReadN(n int) (b []byte, err error) {
	var l int
	b = make([]byte, n)
	if l, err = c.Conn.Read(b); err == nil {
		b = b[:l]
	}
	return
}

func (c *conn) Read(p []byte) (n int, err error) {
	return c.Conn.Read(p)
}

func (c *conn) Write(p []byte) (n int, err error) {
	return c.Conn.Write(p)
}

func (c *conn) Close() error {
	return c.Conn.Close()
}

type apphostListener struct{ *astral.Listener }

func (a *apphostListener) Next() (query QueryData, err error) {
	var q *astral.QueryData
	q, err = a.Listener.Next()
	if err != nil {
		return
	}
	query = &queryData{query: q}
	return
}

func (a *apphostListener) Close() error {
	return a.Listener.Close()
}

type queryData struct{ query *astral.QueryData }

func (q *queryData) Caller() string {
	return q.query.RemoteIdentity().String()
}

func (q *queryData) Accept() (c Conn, err error) {
	cc := &conn{}
	cc.Conn, err = q.query.Accept()
	c = cc
	return
}

func (q *queryData) Reject() error {
	return q.query.Reject()
}

func (q *queryData) Query() string {
	return q.query.Query()
}
