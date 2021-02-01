/*************************************************************************************
 * Copyright (C) 2014-2021 GENERAL BYTES s.r.o. All rights reserved.
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
package com.generalbytes.batm.server.extensions.extra.nano.wallets.paperwallet;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.IPaperWallet;
import com.generalbytes.batm.server.extensions.IPaperWalletGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import uk.oczadly.karl.jnano.util.WalletUtil;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.HexData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NanoPaperWalletGenerator implements IPaperWalletGenerator {
    private static final Logger log = LoggerFactory.getLogger("batm.master.extensions.NanoPaperWalletGenerator");
    private static final String MESSAGE = "We have attached a QR code with your address. Please use your QR code to add more funds to your account. Your address is ";

    @Override
    public IPaperWallet generateWallet(String cryptoCurrency, String oneTimePassword, String userLanguage,
            boolean shouldBeVanity) {
        final int imagesize = 400;
        NanoPaperWallet paperwallet = new NanoPaperWallet();
        HexData privateKey;
        try {
            privateKey = WalletUtil.generateRandomKey();
        } catch (NoSuchAlgorithmException e) {
            log.error("Could not generate paper wallet", e);
            return null;
        }
        NanoAccount account = NanoAccount.fromPrivateKey(privateKey);
        String address = account.toAddress();
        byte[] image = generateQR("nano:" + address, imagesize);

        paperwallet.setCryptoCurrency(CryptoCurrency.NANO.getCode());
        paperwallet.setMessage(MESSAGE + address);
        paperwallet.setFileExtension("png");
        paperwallet.setAddress(address);
        paperwallet.setContentType("image/png");
        paperwallet.setContent(image);
        paperwallet.setPrivateKey(privateKey.toHexString());
        return paperwallet;
    }

    public byte[] generateQR(String text, int size) {
        Hashtable hintMap = new Hashtable();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size, hintMap);
            int matrixWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, matrixWidth, matrixWidth);
            // Paint and save the image using the ByteMatrix
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < matrixWidth; i++) {
                for (int j = 0; j < matrixWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream stream = new MemoryCacheImageOutputStream(baos);
            ImageIO.write(image, "png", stream);
            stream.close();
            return baos.toByteArray();
        } catch (WriterException e) {
            log.error("Error", e);
        } catch (IOException e) {
            log.error("Error", e);
        }
        return null;
    }

/*    public static void main(String[] args) {
        NanoPaperWalletGenerator paperWalletGenerator = new NanoPaperWalletGenerator();
        IPaperWallet paperWallet = paperWalletGenerator.generateWallet(CryptoCurrency.NANO.getCode(), "", "", false);

        byte[] publicKeyQR = paperWallet.getContent();
        ByteArrayInputStream bis = new ByteArrayInputStream(publicKeyQR);
        try {
            BufferedImage bImage = ImageIO.read(bis);
            ImageIO.write(bImage, "png", new File("public_key.png"));

            byte[] privateKeyQR = paperWalletGenerator.generateQR(paperWallet.getPrivateKey(), 400);
            ByteArrayInputStream bisPrivate = new ByteArrayInputStream(privateKeyQR);
            BufferedImage bImage1 = ImageIO.read(bisPrivate);
            ImageIO.write(bImage1, "png", new File("private_key.png"));

            System.out.println("images created");
        } catch (IOException e) {
            System.out.println(e.toString());
            return;
        }
    }
*/
}