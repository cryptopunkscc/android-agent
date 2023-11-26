package astral

import (
	"context"
	"fmt"
	"github.com/cryptopunkscc/astrald/node"
	"github.com/cryptopunkscc/astrald/node/assets"
	"gorm.io/driver/sqlite"
	"log"
	"os"
	"time"
)

var identity string
var astralCtx context.Context
var stop context.CancelFunc

func Start(
	astralRoot string,
) (err error) {
	if err = os.MkdirAll(astralRoot, 0700); err != nil {
		return
	}
	assets.DBOpener = sqlite.Open

	log.Println("astral staring")

	// Set up app execution context
	astralCtx, stop = context.WithCancel(context.Background())

	// start the node
	var n *node.CoreNode
	if n, err = node.NewCoreNode(astralRoot); err != nil {
		err = fmt.Errorf("init error: %v", err)
		return
	}

	identity = n.Identity().String()

	// Run the node
	if err = n.Run(astralCtx); err != nil {
		err = fmt.Errorf("run error: %v", err)
		return
	}

	time.Sleep(300 * time.Millisecond)

	log.Println("astral stopped")

	return
}

func Identity() string {
	return identity
}

func Stop() {
	stop()
}
