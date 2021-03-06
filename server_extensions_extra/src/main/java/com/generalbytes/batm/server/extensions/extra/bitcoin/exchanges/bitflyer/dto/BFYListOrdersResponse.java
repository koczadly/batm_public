/*************************************************************************************
 * Copyright (C) 2014-2020 GENERAL BYTES s.r.o. All rights reserved.
 *
 * This software may be distributed and modified under the terms of the GNU
 * General Public License version 2 (GPL2) as published by the Free Software
 * Foundation and appearing in the file GPL2.TXT included in the packaging of
 * this file. Please note that GPL2 Section 2[b] requires that all works based
 * on this software must also be made publicly available under the terms of
 * the GPL2 ("Copyleft").
 *
 * Contact information
 * -------------------
 *
 * GENERAL BYTES s.r.o.
 * Web      :  http://www.generalbytes.com
 *
 ************************************************************************************/
package com.generalbytes.batm.server.extensions.extra.bitcoin.exchanges.bitflyer.dto;

import java.math.BigDecimal;

public class BFYListOrdersResponse {
    public long id;
    public String child_order_id;
    public String product_code;
    public String side;
    public String child_order_type;
    public BigDecimal price;
    public BigDecimal average_price;
    public BigDecimal size;
    public String child_order_state;
    public String expire_date;
    public String child_order_date;
    public String child_order_acceptance_id;
    public BigDecimal outstanding_size;
    public BigDecimal cancel_size;
    public BigDecimal executed_size;
    public BigDecimal total_commission;
}
