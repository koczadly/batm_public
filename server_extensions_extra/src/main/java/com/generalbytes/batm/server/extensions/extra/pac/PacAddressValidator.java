/*************************************************************************************
 * Copyright (C) 2014-2016 GENERAL BYTES s.r.o. All rights reserved.
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
package com.generalbytes.batm.server.extensions.extra.pac;
import com.generalbytes.batm.server.coinutil.AddressFormatException;
import com.generalbytes.batm.server.coinutil.Base58;
import com.generalbytes.batm.server.extensions.ICryptoAddressValidator;

public class PacAddressValidator implements ICryptoAddressValidator {

        @Override
        public boolean isAddressValid(String address) {
            //For whatever reason this may occur, cover the case
            if (address.startsWith("P")) {
                try {
                    if(address.length() == 34) {
                        Base58.decodeToBigInteger(address);
                        Base58.decodeChecked(address);
                        return true;
                    } else {
                        return false;
                    }
                } catch (AddressFormatException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        }
        @Override
        public boolean isPaperWalletSupported() {
            return false;
        }
        @Override
        public boolean mustBeBase58Address() {
            return true;
        }
    }