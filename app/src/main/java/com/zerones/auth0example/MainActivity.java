package com.zerones.auth0example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by kishan on 5/2/16.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int
    final String TAG = getClass().getName();
    private String StripeKey = "pk_test_4lubEEDkqrlfF7vzUEPmfIFb";

    private PreferenceHelper preferenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnPurChase).setOnClickListener(this);
        findViewById(R.id.btnLogOut).setOnClickListener(this);
        preferenceHelper = new PreferenceHelper(this);
    }

    private void getCardInformation() {
        Intent scanIntent = new Intent(MainActivity.this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true); // default: false

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String resultStr;
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            resultStr = "Card Number: " + scanResult.getFormattedCardNumber() + "\n";

            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );

            if (scanResult.isExpiryValid()) {
                resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
            }

            if (scanResult.cvv != null) {
                // Never log or display a CVV
                resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
            }

            if (scanResult.postalCode != null) {
                resultStr += "Postal Code: " + scanResult.postalCode + "\n";
            }

            if (scanResult.cardholderName != null) {
                resultStr += "Cardholder Name : " + scanResult.cardholderName + "\n";
            }
            //Toast.makeText(MainActivity.this, resultStr, Toast.LENGTH_LONG).show();
            Card card = new Card(scanResult.getFormattedCardNumber(), scanResult.expiryMonth, scanResult.expiryYear, scanResult.cvv);
            try {
                Stripe stripe = new Stripe(StripeKey);
                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(com.stripe.android.model.Token token) {
                                // Send token to your server
                                //Toast.makeText(MainActivity.this, token.getId(), Toast.LENGTH_LONG).show();
                                preferenceHelper.putCardToken(token.getId());
                                Toast.makeText(MainActivity.this, "Purchase SuccessFull", Toast.LENGTH_SHORT).show();
                            }

                            public void onError(Exception error) {
                                // Show localized error message
                                // Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
            } catch (Exception e) {

            }

        } else {
            resultStr = "Scan was canceled.";

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogOut:
                preferenceHelper.putUserId("");
                preferenceHelper.putCardToken("");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.btnPurChase:
                if (TextUtils.isEmpty(preferenceHelper.getCardToken())) {
                    getCardInformation();
                } else {
                    Toast.makeText(this, "Purchase SuccessFull", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
