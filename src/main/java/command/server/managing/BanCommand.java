package command.server.managing;

import command.ICommand;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class BanCommand implements ICommand {
    private final String HELP = "Ban un / plusieurs utilisateur(s) du seveur. \nUsage : `!ban @UserA @UserB @UserC ... Durée Raison `";
    private final String BAN_MESSAGE = "Tu as été banni car : ";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length != 0 && args[0].equals("help") || args.length < 3) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        List<Member> targetedUsers = event.getMessage().getMentionedMembers();
        for (Member curr : targetedUsers) {
            event.getGuild().getController().ban(curr,Integer.parseInt(args[args.length -2]),BAN_MESSAGE + args[args.length -1]).queue();
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