package discord.bot.utils.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class CustomAudioLoadResultHandler implements AudioLoadResultHandler {

    private AudioPlayer player;
    private AudioManager guildAudioManager;
    private TextChannel chanToWrite;
    private VoiceChannel targetVoicelChannel;

    private final String SOUND_NOT_FOUND = "Track not found.";
    private final String SOUND_PLAY_FAILED = "Can't play the track at the moment.";

    public void setTargetVoicelChannel(VoiceChannel targetVoicelChannel) {
        this.targetVoicelChannel = targetVoicelChannel;
    }

    public void setChanToWrite(TextChannel chanToWrite) {
        this.chanToWrite = chanToWrite;
    }

    public void setPlayer(AudioPlayer player) { this.player = player; }

    public void setGuildAudioManager(AudioManager guildAudioManager) {
        this.guildAudioManager = guildAudioManager;
    }

    public AudioManager getGuildAudioManager() { return this.guildAudioManager; }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        AudioPlayerSendHandler handler = new AudioPlayerSendHandler(player);
        guildAudioManager.setSendingHandler(handler);
        guildAudioManager.openAudioConnection(targetVoicelChannel);
        player.playTrack(audioTrack);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {

    }

    @Override
    public void noMatches() {
        if(chanToWrite != null) {
            this.chanToWrite.sendMessage(SOUND_NOT_FOUND).queue();
        }
    }

    @Override
    public void loadFailed(FriendlyException e) {
        e.printStackTrace();
        if(chanToWrite != null) {
            this.chanToWrite.sendMessage(SOUND_PLAY_FAILED).queue();
        }
    }
}