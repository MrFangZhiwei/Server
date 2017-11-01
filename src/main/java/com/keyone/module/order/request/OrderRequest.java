package com.keyone.module.order.request;

import com.keyone.serial.Serializer;

public class OrderRequest extends Serializer
{
	private String content;

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	@Override
	protected void read()
	{
		this.content = readString();
	}

	@Override
	protected void write()
	{
		writeString(content);
	}

}
