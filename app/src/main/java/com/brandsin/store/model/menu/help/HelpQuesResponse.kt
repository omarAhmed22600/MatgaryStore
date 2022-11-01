package com.brandsin.user.model.menu.help

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class HelpQuesResponse(

	@field:SerializedName("data")
	val quesList: List<HelpQuesItem?>? = null,

	@field:SerializedName("success")
	val isSuccess: Boolean? = null
)

data class HelpQuesItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("content")
	val content: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		parcel.readString(),
		parcel.readString()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(id)
		parcel.writeString(title)
		parcel.writeString(slug)
		parcel.writeString(content)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<HelpQuesItem> {
		override fun createFromParcel(parcel: Parcel): HelpQuesItem {
			return HelpQuesItem(parcel)
		}

		override fun newArray(size: Int): Array<HelpQuesItem?> {
			return arrayOfNulls(size)
		}
	}
}
