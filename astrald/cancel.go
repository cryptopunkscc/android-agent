package astral

import "context"

type Worker struct {
	ctx    context.Context
	cancel context.CancelFunc
	active bool
}

func NewWorker(ctx context.Context, cancel context.CancelFunc) *Worker {
	worker := &Worker{ctx: ctx, cancel: cancel}
	go func() {
		worker.active = true
		<-ctx.Done()
		worker.active = false
	}()
	return worker
}

func (c *Worker) IsActive() bool {
	return c.active
}

func (c *Worker) Cancel() {
	c.cancel()
}
