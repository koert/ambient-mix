/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/koert/project/koert/dev/android/new/ambient-mix/src/net/kazed/ambient/service/PlayerService.aidl
 */
package net.kazed.ambient.service;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
public interface PlayerService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements net.kazed.ambient.service.PlayerService
{
private static final java.lang.String DESCRIPTOR = "net.kazed.ambient.service.PlayerService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an PlayerService interface,
 * generating a proxy if needed.
 */
public static net.kazed.ambient.service.PlayerService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof net.kazed.ambient.service.PlayerService))) {
return ((net.kazed.ambient.service.PlayerService)iin);
}
return new net.kazed.ambient.service.PlayerService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_start:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
this.start(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_stop:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
this.stop(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_pauseAll:
{
data.enforceInterface(DESCRIPTOR);
this.pauseAll();
reply.writeNoException();
return true;
}
case TRANSACTION_stopAll:
{
data.enforceInterface(DESCRIPTOR);
this.stopAll();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements net.kazed.ambient.service.PlayerService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void start(long audioFragmentId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(audioFragmentId);
mRemote.transact(Stub.TRANSACTION_start, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void stop(long audioFragmentId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(audioFragmentId);
mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void pauseAll() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pauseAll, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void stopAll() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopAll, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_start = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_stop = (IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_pauseAll = (IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_stopAll = (IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void start(long audioFragmentId) throws android.os.RemoteException;
public void stop(long audioFragmentId) throws android.os.RemoteException;
public void pauseAll() throws android.os.RemoteException;
public void stopAll() throws android.os.RemoteException;
}
