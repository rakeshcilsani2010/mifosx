package org.mifosng.platform.loanschedule.domain;

import java.math.BigDecimal;

import org.mifosng.platform.currency.domain.MonetaryCurrency;
import org.mifosng.platform.currency.domain.Money;
import org.mifosng.platform.loan.domain.LoanProductRelatedDetail;

public class PmtCalculator {

	public Money calculatePaymentForOnePeriodFrom(LoanProductRelatedDetail loanScheduleInfo, BigDecimal periodInterestRateForRepaymentPeriod, MonetaryCurrency monetaryCurrency) {
		// TODO - check if going to doubles is a problem
		double interestRateFraction = periodInterestRateForRepaymentPeriod.doubleValue();
		
		double futureValue = 0;
		double numberOfPeriods = loanScheduleInfo.getNumberOfRepayments().doubleValue();
		double principal = loanScheduleInfo.getPrincipal().getAmount().multiply(BigDecimal.valueOf(-1)).doubleValue();
		
		// work out the total payment (principal + interest components) for each installment due.
		// use the period type and interest as expressed e.g. 2% per month or 24% per year even is 'daily' interest calculation is selected
		double paymentPerInstallment = pmt(interestRateFraction, numberOfPeriods, principal, futureValue, false);
		
		return Money.of(monetaryCurrency, BigDecimal.valueOf(paymentPerInstallment));
	}
	
	public Money calculateTotalRepaymentFrom(LoanProductRelatedDetail loanScheduleInfo, BigDecimal periodInterestRateForRepaymentPeriod, MonetaryCurrency monetaryCurrency) {
		// TODO - check if going to doubles is a problem
		double interestRateFraction = periodInterestRateForRepaymentPeriod.doubleValue();
		
		double futureValue = 0;
		double numberOfPeriods = loanScheduleInfo.getNumberOfRepayments().doubleValue();
		double principal = loanScheduleInfo.getPrincipal().getAmount().multiply(BigDecimal.valueOf(-1)).doubleValue();
		
		// work out the total payment (principal + interest components) for each installment due.
		// use the period type and interest as expressed e.g. 2% per month or 24% per year even is 'daily' interest calculation is selected
		double paymentPerInstallment = pmt(interestRateFraction, numberOfPeriods, principal, futureValue, false);
		
		double totalRepayment = paymentPerInstallment * loanScheduleInfo.getNumberOfRepayments();
		
		return Money.of(monetaryCurrency, BigDecimal.valueOf(totalRepayment));
	}

	/**
	 * PMT calculates a fixed monthly payment to be paid by borrower every 'period' to ensure loan is paid off in full (with interest).
	 * 
	 * This monthly payment c depends upon
	 * the monthly interest rate r (expressed as a fraction, not a percentage,
	 * i.e., divide the quoted yearly percentage rate by 100 and by 12 to obtain
	 * the monthly interest rate), the number of monthly payments N called the
	 * loan's term, and the amount borrowed P known as the loan's principal; c
	 * is given by the formula:
	 * 
	 * c = (r / (1 - (1 + r)^-N))P
	 * 
	 * @param interestRateFraction
	 * @param numberOfPayments
	 * @param principal
	 * @param futureValue
	 * @param type
	 */
	private static double pmt(double interestRateFraction, double numberOfPayments, double principal, double futureValue, boolean type) {
		double payment = 0;
		if (interestRateFraction == 0) {
			payment = -1 * (futureValue + principal) / numberOfPayments;
		} else {
			double r1 = interestRateFraction + 1;
			payment = (futureValue + principal * Math.pow(r1, numberOfPayments)) * interestRateFraction
					/ ((type ? r1 : 1) * (1 - Math.pow(r1, numberOfPayments)));
		}
		return payment;
	}
}