package astral

import (
	"context"
	"fmt"
	"github.com/cryptopunkscc/go-warpdrive/adapter/apphost"
	"github.com/cryptopunkscc/go-warpdrive/android"
	"log"
)

var stopWarpdrive context.CancelFunc

func StartWarpdrive(dir string) (err error) {
	log.Println("warpdrive starting")

	var ctx context.Context

	ctx, stopWarpdrive = context.WithCancel(context.Background())
	api := apphost.Adapter{}
	server := android.Server(dir, api)

	err = server.Run(ctx, api)
	if err != nil {
		err = fmt.Errorf("wrapdrive error: %v", err)
		return
	}

	log.Println("warpdrive stopped")
	return
}

func StopWarpdrive() {
	stopWarpdrive()
}
