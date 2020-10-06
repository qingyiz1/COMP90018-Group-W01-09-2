package com.example.pet;

import android.bluetooth.BluetoothDevice;

public class BluetoothPair {
    private BluetoothDevice device;

    public BluetoothPair(BluetoothDevice device) {
        this.device = device;
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }
}
