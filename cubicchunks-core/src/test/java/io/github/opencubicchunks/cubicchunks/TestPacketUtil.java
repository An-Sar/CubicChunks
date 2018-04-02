/*
 *  This file is part of Cubic Chunks Mod, licensed under the MIT License (MIT).
 *
 *  Copyright (c) 2015 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package io.github.opencubicchunks.cubicchunks;

import static org.junit.Assert.assertEquals;

import io.github.opencubicchunks.cubicchunks.core.util.PacketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

public class TestPacketUtil {

    @Test
    public void testSignedVarInts() {
        testRange(-128, 128, 0);
        testRange(-128, 128, 6);
        testRange(-128, 128, 12);
        testRange(-128, 128, 18);
        testRange(-128, 128, 24);
        testRange(Integer.MIN_VALUE, Integer.MIN_VALUE + 128, 0);
        testRange(Integer.MAX_VALUE - 128, Integer.MAX_VALUE, 0);
    }

    private void testRange(int min, int max, int bitshift) {
        // 5* because max 5 bytes per int
        ByteBuf buf = Unpooled.wrappedBuffer(new byte[5 * (max - min + 1)]);
        buf.writerIndex(0);
        for (long i = min; i <= max; i++) {
            int val = (int) i << bitshift;
            PacketUtils.writeSignedVarInt(buf, val);
        }
        buf.readerIndex(0);
        for (long i = min; i <= max; i++) {
            int val = (int) i << bitshift;
            assertEquals(val, PacketUtils.readSignedVarInt(buf));
        }
    }
}
