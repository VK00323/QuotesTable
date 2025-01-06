package com.example.data.websocket.dataSource

import javax.inject.Inject

class QuotesLabelLocalDataSource @Inject constructor() : IQuotesLabelLocalDataSource {

    override fun getQuotesLabel(): List<String> {
        return listOf(
            "SP500.IDX", "AAPL.US", "RSTI", "GAZP", "MRKZ", "RUAL", "HYDR", "MRKS", "SBER",
            "FEES", "TGKA", "VTBR", "ANH.US", "VICL.US", "BURG.US", "NBL.US", "YETI.US", "WSFS.US",
            "NIO.US", "DXC.US", "MIC.US", "HSBC.US", "EXPN.EU", "GSK.EU", "SHP.EU", "MAN.EU",
            "DB1.EU", "MUV2.EU", "TATE.EU", "KGF.EU", "MGGT.EU", "SGGD.EU"
        )
    }
}