package astral

import (
	"context"
	"fmt"
	"github.com/cryptopunkscc/astrald/node"
	"log"
	"os"
	"path/filepath"
	"time"
)

var n *node.CoreNode
var identity string
var stop context.CancelFunc

func Start(
	dir string,
	// handlers Handlers,
	// bluetooth Bluetooth,
) (err error) {
	astralRoot := filepath.Join(dir, "astrald")
	if err = os.MkdirAll(astralRoot, 0700); err != nil {
		return
	}

	log.Println("Staring astrald")
	//astral.ListenProtocol = "tcp"

	// Set up app execution context
	var ctx context.Context
	ctx, stop = context.WithCancel(context.Background())

	// start the node
	if n, err = node.NewCoreNode(astralRoot); err != nil {
		err = fmt.Errorf("init error: %v", err)
		return
	}

	identity = n.Identity().String()

	// Run the node
	if err = n.Run(ctx); err != nil {
		err = fmt.Errorf("run error: %v", err)
		return
	}

	time.Sleep(300 * time.Millisecond)

	log.Println("Astral stopped")

	return nil
}

func Identity() string {
	return identity
}

func Stop() {
	stop()
}
