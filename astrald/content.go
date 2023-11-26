package astral

import (
	rpc "github.com/cryptopunkscc/go-apphost-jrpc"
	"github.com/cryptopunkscc/go-apphost-jrpc/android/content"
)

func StartContentResolver(factory ContentResolverFactory) error {
	return rpc.Server[any]{
		Ctx: astralCtx,
		Handler: func(c *rpc.Conn) any {
			if c != nil {
				return factory.Create(&conn{&c.Conn})
			} else {
				return factory.Create(nil)
			}
		},
	}.Run()
}

type ContentResolverFactory interface {
	Create(conn Conn) ContentResolver
}

type ContentResolver interface {
	String() string
	Info(uri string) (files *ContentInfo, err error)
	Reader(uri string, offset int64) (err error)
}

type ContentInfo content.Info
