package command.server.managing;

import command.ICommand;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class KickCommand implements ICommand {
    private final String HELP = "Kick un / plusieurs utilisateur(s) du seveur. \nUsage : `!kick @UserA @UserB @UserC ... Raison `";
    private final String KICK_MESSAGE = "Tu as été exclu car : ";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length != 0 && args[0].equals("help") || args.length < 2) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        List<Member> targetedUsers = event.getMessage().getMentionedMembers();
        for (Member curr : targetedUsers) {
            event.getGuild().getController().kick(curr,KICK_MESSAGE + args[args.length -1]).queue();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        if(!success) {
            event.getTextChannel().sendMessage(help()).queue();
        }
    }
}