package astral

import "context"

type Worker struct {
	ctx    context.Context
	cancel context.CancelFunc
	start  func() error
	err    error
	active bool
}

func newWorker() *Worker {
	ctx, cancel := context.WithCancel(context.Background())
	worker := &Worker{ctx: ctx, cancel: cancel}
	return worker
}

func (w *Worker) Start() (err error) {
	w.active = true
	err = w.start()
	w.err = err
	w.active = false
	return
}

func (w *Worker) StartAsync() {
	go w.Start()
}

func (w *Worker) IsActive() bool {
	return w.active
}

func (w *Worker) Cancel() {
	w.cancel()
}

func (w *Worker) Await() {
	<-w.ctx.Done()
}
