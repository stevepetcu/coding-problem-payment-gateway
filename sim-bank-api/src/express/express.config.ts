import express, {Application} from 'express';
import {z} from 'zod';

enum PAYMENT_STATUSES {
  SETTLED = 'SETTLED',
  REJECTED = 'REJECTED',
}

// TODO: refine this validation -- after all, we have to anticipate that
//  this bank will still be around post-2100, like all bad pieces of code.
const CreatePaymentOrderRequest = z.object({
  card_number: z.string().min(15).max(19),
  card_exp_month: z.number().min(1).max(12),
  card_exp_year: z.number().min(2023).max(2100),
  card_cvv: z.string().min(3).max(4),
  payment_amount_in_minor: z.number().positive(),
  payment_currency: z.string().length(3),
  reference: z.string().max(50).optional(),
});

const ExpressConfig = (): Application => {
  const app = express();

  app.use(express.json());

  app.post('/payment-orders', (req, res) => {
    const reqBody = req.body;

    const reqValidation = CreatePaymentOrderRequest.safeParse(reqBody);

    if (!reqValidation.success) {
      // TODO: improve error messages.
      res.status(400).send({
        error: 'Bad Request',
        details: reqValidation.error.format()
      });

      return;
    }

    let paymentStatus;

    switch (reqBody.reference) {
      // Add more test cases here:
      case 'test:rejected': {
        paymentStatus = PAYMENT_STATUSES.REJECTED;
        break;
      }
      default: {
        paymentStatus = PAYMENT_STATUSES.SETTLED;
      }
    }

    res.status(200).send({status: paymentStatus});
  });

  return app;
};

export default ExpressConfig;
