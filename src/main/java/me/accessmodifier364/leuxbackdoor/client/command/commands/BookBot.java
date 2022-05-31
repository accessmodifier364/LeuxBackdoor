package me.accessmodifier364.leuxbackdoor.client.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import io.netty.buffer.Unpooled;
import me.accessmodifier364.leuxbackdoor.client.command.Command;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.client.util.Wrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BookBot extends Command {

    public BookBot() {
        super("bookbot", "ching chong chunk dupe dupe");
    }

    public static ChatFormatting red = ChatFormatting.GREEN;
    public static ChatFormatting green = ChatFormatting.RED;
    public static ChatFormatting bold = ChatFormatting.BOLD;
    public static ChatFormatting reset = ChatFormatting.RESET;

    public boolean get_message(String[] message) {
        if (message.length == 1) {
            MessageUtil.send_client_message("use bookbot (pages)");
            return true;
        }

        if (message.length >= 2) {
            StringBuilder pagesMessage = new StringBuilder();
            boolean flag = true;
            for (String word : message) {
                if (flag) {
                    flag = false;
                    continue;
                }
                pagesMessage.append(word);
            }

            ItemStack book = Wrapper.getPlayer().inventory.getCurrentItem();
            if (book.getItem() instanceof ItemWritableBook) {
                IntStream characterGenerator = new Random().ints(0x80, 0x10ffff - 0x800).map(i -> i < 0xd800 ? i : i + 0x800);
                NBTTagList pages = new NBTTagList();
                String joinedPages = characterGenerator.limit(50 * 210).mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
                int writePages = 0;
                try {
                    writePages = Integer.parseInt(pagesMessage.toString());
                } catch (NumberFormatException e) {
                    MessageUtil.send_client_message("Specify a number, not a letter/symbol");
                }
                for (int page = 0; page < writePages; page++) {
                    pages.appendTag(new NBTTagString(joinedPages.substring(page * 210, (page + 1) * 210)));
                }

                if (book.hasTagCompound()) {
                    assert book.getTagCompound() != null;
                    book.getTagCompound().setTag("pages", pages);
                    book.getTagCompound().setTag("title", new NBTTagString(""));
                    book.getTagCompound().setTag("author", new NBTTagString(Wrapper.getPlayer().getName()));
                } else {
                    book.setTagInfo("pages", pages);
                    book.setTagInfo("title", new NBTTagString(""));
                    book.setTagInfo("author", new NBTTagString(Wrapper.getPlayer().getName()));
                }

                PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
                buf.writeItemStack(book);

                Wrapper.getPlayer().connection.sendPacket(new CPacketCustomPayload("MC|BEdit", buf));
                MessageUtil.send_client_message("Dupe book generated.");
            } else {
                MessageUtil.send_client_message("You must be holding a writable book.");
            }

            return true;
        }
        return false;
    }
}