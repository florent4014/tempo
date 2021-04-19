package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.helper.MusicPlayerRemote;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class PlayerBottomSheetViewModel extends AndroidViewModel {
    private static final String TAG = "PlayerBottomSheetViewModel";

    private SongRepository songRepository;
    private QueueRepository queueRepository;

    private LiveData<List<Song>> queueSong;

    public PlayerBottomSheetViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        queueRepository = new QueueRepository(application);

        queueSong = queueRepository.getLiveQueue();
    }

    public LiveData<List<Song>> getQueueSong() {
        return queueSong;
    }

    public void setFavorite() {
        Song song = MusicPlayerRemote.getCurrentSong();
        song.setFavorite(!song.isFavorite());
        songRepository.setFavoriteStatus(song);
    }

    public void orderSongAfterSwap(List<Song> songs) {
        queueRepository.insertAllAndStartNew(songs);
    }

    public void removeSong(int position) {
        queueRepository.deleteByPosition(position);
    }
}