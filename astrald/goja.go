package astral

import (
	"context"
	"github.com/cryptopunkscc/go-astral-js"
	goja2 "github.com/cryptopunkscc/go-astral-js/goja"
	"github.com/dop251/goja"
	"log"
)

func RunGojaJsBackend(source string) {
	vm := goja.New()

	err := goja2.Bind(vm, astraljs.NewAppHostFlatAdapter())
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

	ctx := context.Background()
	<-ctx.Done()
}
