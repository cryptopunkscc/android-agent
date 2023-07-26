package astral

import (
	"context"
	"github.com/cryptopunkscc/go-astral-js"
	goja2 "github.com/cryptopunkscc/go-astral-js/goja"
	"github.com/dop251/goja"
	"log"
)

func RunGojaJsBackend(source string) *Worker {
	ctx, cancel := context.WithCancel(context.Background())
	go runGojaJsBackend(ctx, source)
	return NewWorker(ctx, cancel)
}

func runGojaJsBackend(ctx context.Context, source string) {
	vm := goja.New()

	appHost := astraljs.NewAppHostFlatAdapter()
	err := goja2.Bind(vm, appHost)
	if err != nil {
		log.Fatal(err)
	}

	// inject apphost client js lib
	_, err = vm.RunString(astraljs.AppHostJsClient())
	if err != nil {
		log.Fatal(err)
	}

	// start js application backend
	_, err = vm.RunString(source)
	if err != nil {
		log.Fatal(err)
	}
	<-ctx.Done()
	vm.Interrupt(nil)
	astraljs.CloseAppHostFlatAdapter(appHost)
}
