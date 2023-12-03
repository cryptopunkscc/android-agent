package astral

import (
	"context"
	rpc "github.com/cryptopunkscc/go-apphost-jrpc"
	"github.com/cryptopunkscc/go-apphost-jrpc/android"
)

func StartNotifier(n android.NotifyServiceApi) error {
	return rpc.Server[any]{
		Handler: func(ctx context.Context, conn *rpc.Conn) any { return n },
	}.Run(astralCtx)
}

func StartContentResolver(factory ContentResolverFactory) error {
	return rpc.Server[any]{
		Handler: func(ctx context.Context, c *rpc.Conn) any {
			if c != nil {
				return factory.Create(&conn{Conn: c})
			} else {
				return factory.Create(nil)
			}
		},
	}.Run(astralCtx)
}

type ContentResolverFactory interface {
	Create(conn Conn) android.ContentServiceApi
}
