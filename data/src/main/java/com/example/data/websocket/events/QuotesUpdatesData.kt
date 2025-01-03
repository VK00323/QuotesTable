package com.example.data.websocket.events

import com.google.gson.annotations.SerializedName

data class QuotesUpdatesData(
    @SerializedName("c") val c: String, // Ticker
    @SerializedName("ltr") val ltr: String?, // Exchange of the latest trade
    @SerializedName("name") val name: String?, // Name of security
    @SerializedName("name2") val name2: String?, // Security name in Latin
    @SerializedName("bbp") val bbp: Double?, // Best bid
    @SerializedName("bbc") val bbc: String?, // Designations of the best bid changes
    @SerializedName("bbs") val bbs: Int?, // Best bid size
    @SerializedName("bbf") val bbf: Double?, // Best bid volume
    @SerializedName("bap") val bap: Double?, // Best offer
    @SerializedName("bac") val bac: String?, // Best offer change mark
    @SerializedName("bas") val bas: Int?, // Best offer size
    @SerializedName("baf") val baf: Double?, // Best offer volume
    @SerializedName("pp") val pp: Double?, // Previous closing
    @SerializedName("op") val op: Double?, // Opening price
    @SerializedName("ltp") val ltp: Double?, // Last trade price
    @SerializedName("lts") val lts: Int?, // Last trade size
    @SerializedName("ltt") val ltt: String?, // Time of last trade
    @SerializedName("chg") val chg: Double?, // Change in price
    @SerializedName("pcp") val pcp: Double?, // Percentage change
    @SerializedName("ltc") val ltc: String?, // Price change designations
    @SerializedName("mintp") val minTradePricePerDay: Double?, // Minimum trade price per day
    @SerializedName("maxtp") val maxTradePricePerDay: Double?, // Maximum trade price per day
    @SerializedName("vol") val vol: Int?, // Trade volume per day in pcs
    @SerializedName("vlt") val vlt: Double?, // Trade volume in currency
    @SerializedName("yld") val yld: Double?, // Yield to maturity
    @SerializedName("acd") val acd: Double?, // Accumulated coupon interest
    @SerializedName("fv") val fv: Double?, // Face value
    @SerializedName("mtd") val mtd: String?, // Payment Date
    @SerializedName("cpn") val cpn: Double?, // Coupon
    @SerializedName("cpp") val cpp: Int?, // Coupon period
    @SerializedName("ncd") val ncd: String?, // Next coupon date
    @SerializedName("ncp") val ncp: String?, // Latest coupon date
    @SerializedName("dpd") val dpd: Double?, // Purchase margin
    @SerializedName("dps") val dps: Double?, // Short sale margin
    @SerializedName("trades") val trades: Int?, // Number of trades
    @SerializedName("min_step") val minStep: Double?, // Minimum price increment
    @SerializedName("step_price") val stepPrice: Double?, // Price increment
)