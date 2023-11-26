package astral

import (
	"context"
	"fmt"
	"github.com/cryptopunkscc/go-warpdrive/android"
	"log"
)

var stopWarpdrive context.CancelFunc

func StartWarpdrive(cache string, storage string) (err error) {
	log.Println("warpdrive starting")

	var ctx context.Context

	ctx, stopWarpdrive = context.WithCancel(astralCtx)

	err = android.Server(ctx, cache, storage)

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
