package contacts

import (
	"context"
	"encoding/json"
	"github.com/cryptopunkscc/astrald/log"
	"github.com/cryptopunkscc/astrald/net"
	"github.com/cryptopunkscc/astrald/node/assets"
	"github.com/cryptopunkscc/astrald/node/modules"
	"github.com/cryptopunkscc/astrald/sig"
	"time"
)

const ServiceName = "contacts"

func init() {
	if err := modules.RegisterModule(ServiceName, Loader{}); err != nil {
		panic(err)
	}
}

type Loader struct{}

func (Loader) Load(node modules.Node, _ assets.Store, log *log.Logger) (modules.Module, error) {
	mod := &Module{node: node, log: log}
	return mod, nil
}

type Module struct {
	node modules.Node
	log  *log.Logger
}

func (m *Module) Run(ctx context.Context) error {
	service, err := m.node.Services().Register(ctx, m.node.Identity(), ServiceName, m)
	if err != nil {
		return err
	}
	<-service.Done()
	return nil
}

func (m *Module) RouteQuery(
	ctx context.Context,
	query net.Query,
	caller net.SecureWriteCloser,
	hints net.Hints,
) (net.SecureWriteCloser, error) {
	if hints.Origin != net.OriginLocal {
		return nil, net.ErrRejected
	}

	c := &handler{m, ctx}
	return net.Accept(query, caller, c.serve)
}

type handler struct {
	*Module
	ctx context.Context
}

func (m *handler) serve(conn net.SecureConn) {
	defer conn.Close()
	defer func() {
		println("finish contacts serve")
	}()

	if err := m.writeContacts(conn); err != nil {
		return
	}

	events := m.node.Network().Events().Subscribe(m.ctx)

	for {
		select {
		case <-events:
			if err := m.writeContacts(conn); err != nil {
				return
			}
		//case <-time.Tick(5 * time.Second):
		//	if err := m.writeContacts(conn); err != nil {
		//		return
		//	}
		case <-m.ctx.Done():
			return
		}
	}
}

func (m *Module) writeContacts(conn net.SecureConn) (err error) {
	contacts := m.contacts()

	marshal, err := json.Marshal(contacts)
	if err != nil {
		m.log.Error("marshal contacts: %v", err)
		return
	}

	_, err = conn.Write(marshal)
	if err != nil {
		m.log.Error("marshal contacts: %v", err)
		return
	}

	return
}

func (m *Module) contacts() (contacts []contact) {
	for _, l := range m.node.Network().Links().All() {
		if l == nil {
			continue
		}
		var idle time.Duration = -1
		var lat time.Duration = -1

		if i, ok := l.Link.(sig.Idler); ok {
			idle = i.Idle().Round(time.Second)
		}

		if l, ok := l.Link.(checkLatency); ok {
			lat = l.Latency()
		}

		c := contact{
			Id:       l.ID(),
			RemoteId: l.RemoteIdentity().String(),
			Remote:   m.node.Resolver().DisplayName(l.RemoteIdentity()),
			Network:  net.Network(l),
			Idle:     idle,
			Since:    time.Since(l.AddedAt()).Round(time.Second),
			Latency:  lat.Round(time.Millisecond),
		}

		contacts = append(contacts, c)
	}
	return
}

type contact struct {
	Id       int
	RemoteId string
	Remote   string
	Network  string
	Idle     time.Duration
	Since    time.Duration
	Latency  time.Duration
}

type checkLatency interface {
	Latency() time.Duration
}
