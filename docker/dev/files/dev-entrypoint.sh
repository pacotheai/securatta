#!/usr/bin/env bash

tmux -2 new-session -d -s securatta
tmux rename-window -t securatta:0 'dev'
tmux select-window -t securatta:0
tmux send-keys -t securatta:0 'cat welcome' C-m

tmux select-window -t securatta:0
tmux -2 attach-session -t securatta
