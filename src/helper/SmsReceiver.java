package helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;


public class SmsReceiver extends BroadcastReceiver{
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		//GPSTrack gps = ((GPSTrack)context);
		
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str= "";
		String message = "";
		String sender = "";
		
		if(bundle != null){
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for(int i=0;i<msgs.length;i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				str += "SMS from " +msgs[i].getOriginatingAddress();
				str += " : ";
				str += msgs[i].getMessageBody().toString();
				str += "n";
				
				message += msgs[i].getMessageBody().toString();
				sender += msgs[i].getOriginatingAddress();
				
				
			}
			
			if(message.contains("hello")){
				//	sms.sendSMS(phone, message);
					GPSTrack gps = new GPSTrack(context.getApplicationContext());
					
					if(gps.canGetLocation){
						
						double longitude = gps.getLongitude();
						double latitude = gps.getLatitude();
						
						SMSHandler sms = new SMSHandler(context.getApplicationContext(),sender,"My Location:: \n"+
								" longitude: " + longitude + " \n"
								+ " latitude: " + latitude + " ");
						
					}else{
						// SMSHandler sms = new SMSHandler(context.getApplicationContext(),msgs[i].getOriginatingAddress(),"Cant detect user location");
						// gps.showAlert();
				//		Intent  i2 = new Intent(context.getApplicationContext(),NotifySMSReceived.class);
					}
					
				}
			
			
			Toast.makeText(context, str, Toast.LENGTH_LONG).show();
			
		}
		
	}
	
	
	
	

}

