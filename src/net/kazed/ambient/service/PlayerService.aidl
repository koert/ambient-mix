package net.kazed.ambient.service;

interface PlayerService {

    void start(in long audioFragmentId);
    void stop(in long audioFragmentId);
    void pauseAll();
    void stopAll();
}
