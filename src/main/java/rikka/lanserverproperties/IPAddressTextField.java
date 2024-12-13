package rikka.lanserverproperties;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class IPAddressTextField extends TextFieldWidget
{
	private final int defaultPort;

	public IPAddressTextField(TextRenderer textRenderer, int x, int y, int width, int height, Text name, int defaultPort)
	{
		super(textRenderer, x, y, width, height, name);
		this.defaultPort = defaultPort;
		this.setText(String.valueOf(this.defaultPort));
		// Check the format, make sure the text is a valid integer
		//this.setChangedListener((text) -> this.setEditableColor(validatePort(text) >= 0 ? 0xFFFFFF : 0xFF0000));
		this.setChangedListener(this::ColLogic);
	}
	private void ColLogic(String text)//条件表达式写这个可读性太差了
	{
		if(validatePort(text) >= 0)
		{
			if(Integer.parseInt(text) >= 49152)//从49152到65535是动态和/或私有端口，不建议使用
			{
				this.setEditableColor(0xed65ff);
				return;
			}
			this.setEditableColor(0xFFFFFF);
			return;
		}
		this.setEditableColor(0xff0000);
	}

	public int getServerPort()
	{
		String portStr = getText();
		return validatePort(portStr) >= 0 ? Integer.parseInt(portStr) : 25565;//不返回默认端口仅仅是为了区分两种情况，无其他含义
	}

	/**
	 * @param text
	 * @return negative if port is invalid, otherwise the port number
	 */
	public static int validatePort(String text)
	{
		boolean valid = true;
		int port = -1;
		try
		{
			if (text.length() > 0)
			{
				port = Integer.parseInt(text);
				if (port < 1024 || port > 65535)//端口小于1024也是不行的,0-1023是公认端口，不能使用，使用游戏会启动失败
				{
					valid = false;
				}
			}
			else
			{
				valid = false;
			}
		}
		catch (NumberFormatException e)
		{
			valid = false;
		}

		return valid ? port : -1;
	}
}
