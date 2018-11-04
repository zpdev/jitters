package co.zpdev.bots.jitters.cmd;

import co.zpdev.core.discord.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;

public class Ban {

    @Command(
            aliases = "ban",
            usage = "!ban {@user} {reason}",
            permission = Permission.BAN_MEMBERS,
            minArgs = 2,
            mentionedMembers = 1,
            autodelete = true
    )
    public void onCommand(Message message, String[] args) {
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Member member = message.getMentionedMembers().get(0);

        message.getGuild().getController().ban(member, 7, reason).queue(s -> message.delete().complete());
        sendLog(member, message.getAuthor(), reason);
    }

    private void sendLog(Member banned, User issuer, String reason) {
        User user = banned.getUser();

        MessageEmbed embed = new EmbedBuilder()
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setDescription("Banned by " + issuer.getAsMention() + ".\n" +
                        "Reason: " + reason)
                .setColor(new Color(240, 71, 71))
                .setFooter("USERID: " + user.getId(), null)
                .setTimestamp(Instant.now()).build();

        Kick.kicked.add(user.getId());
        banned.getGuild().getTextChannelById(314654582183821312L).sendMessage(embed).complete();
    }

}
