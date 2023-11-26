package astral

import (
	rpc "github.com/cryptopunkscc/go-apphost-jrpc"
	"github.com/cryptopunkscc/go-apphost-jrpc/android/notify"
)

func StartNotifier(n Notifier) error {
	return rpc.Server[any]{
		Ctx:     astralCtx,
		Handler: func(conn *rpc.Conn) any { return n },
	}.Run()
}

type Notifier interface {
	String() string
	Create(channel *NotificationChannel) error
	Notify(notifications *Notification) error
}

type NotificationChannel notify.Channel
type Notification notify.Notification
type NotificationProgress notify.Progress
type AndroidIntent notify.Intent
type NotificationAction notify.Action

func (n *Notification) GetContentIntent() *AndroidIntent {
	if n.ContentIntent == nil {
		return nil
	}
	ai := AndroidIntent(*n.ContentIntent)
	return &ai
}

func (n *Notification) GetProgress() *NotificationProgress {
	if n.Progress == nil {
		return nil
	}
	v := NotificationProgress(*n.Progress)
	return &v
}

func (n *Notification) GetAction() *NotificationAction {
	if n.ContentIntent == nil {
		return nil
	}
	v := NotificationAction(*n.Action)
	return &v
}

func (n *NotificationAction) GetContentIntent() *AndroidIntent {
	if n.Intent == nil {
		return nil
	}
	ai := AndroidIntent(*n.Intent)
	return &ai
}
