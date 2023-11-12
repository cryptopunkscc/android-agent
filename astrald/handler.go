package astral

import (
	"context"
	"fmt"
	"github.com/cryptopunkscc/astrald/lib/astral"
	"log"
)

type Handler interface {
	Serve(conn Conn)
	String() string
}

type Handlers interface {
	Next() Handler
}

type HandlersWorker struct{ *Worker }

func NewHandlersWorker(handlers Handlers) (w *HandlersWorker) {
	w = &HandlersWorker{newWorker()}
	r := &handlersRunner{handlers}

	w.start = func() (err error) {
		log.Println("handlers starting")
		err = r.Run(w.ctx)
		if err != nil {
			err = fmt.Errorf("handlers error: %v", err)
			return
		}
		log.Println("handlers stopped")
		return
	}
	return
}

type handlersRunner struct {
	handlers Handlers
}

func (r handlersRunner) Run(ctx context.Context) error {
	for {
		handler := r.handlers.Next()
		if handler == nil {
			break
		}
		port, err := astral.Register(handler.String())
		if err != nil {
			return err
		}
		go func() {
			defer port.Close()
			for q := range port.QueryCh() {
				c, err := q.Accept()
				if err != nil {
					log.Println("Cannot accept query", err)
					continue
				}
				finish := make(chan struct{})
				go func() {
					defer c.Close()
					select {
					case <-ctx.Done():
					case <-finish:
					}
				}()
				go func(c *astral.Conn) {
					defer close(finish)
					w := &conn{c}
					handler.Serve(w)
				}(c)
			}
		}()
	}
	<-ctx.Done()
	return nil
}
