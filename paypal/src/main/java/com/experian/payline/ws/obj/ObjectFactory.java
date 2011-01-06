
package com.experian.payline.ws.obj;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.experian.payline.ws.obj package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ContractLabel_QNAME = new QName("http://obj.ws.payline.experian.com", "label");
    private final static QName _TransactionThreeDSecure_QNAME = new QName("http://obj.ws.payline.experian.com", "threeDSecure");
    private final static QName _PointOfSellComments_QNAME = new QName("http://obj.ws.payline.experian.com", "comments");
    private final static QName _PointOfSellSaleCondURL_QNAME = new QName("http://obj.ws.payline.experian.com", "saleCondURL");
    private final static QName _PointOfSellPrivateLifeURL_QNAME = new QName("http://obj.ws.payline.experian.com", "privateLifeURL");
    private final static QName _PointOfSellEndOfPaymentRedirection_QNAME = new QName("http://obj.ws.payline.experian.com", "endOfPaymentRedirection");
    private final static QName _PointOfSellBuyerMustAcceptSaleCond_QNAME = new QName("http://obj.ws.payline.experian.com", "buyerMustAcceptSaleCond");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.experian.payline.ws.obj
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link VirtualTerminal }
     * 
     */
    public VirtualTerminal createVirtualTerminal() {
        return new VirtualTerminal();
    }

    /**
     * Create an instance of {@link Contract }
     * 
     */
    public Contract createContract() {
        return new Contract();
    }

    /**
     * Create an instance of {@link Transaction }
     * 
     */
    public Transaction createTransaction() {
        return new Transaction();
    }

    /**
     * Create an instance of {@link Details }
     * 
     */
    public Details createDetails() {
        return new Details();
    }

    /**
     * Create an instance of {@link Result }
     * 
     */
    public Result createResult() {
        return new Result();
    }

    /**
     * Create an instance of {@link AddressOwner }
     * 
     */
    public AddressOwner createAddressOwner() {
        return new AddressOwner();
    }

    /**
     * Create an instance of {@link BillingRecord }
     * 
     */
    public BillingRecord createBillingRecord() {
        return new BillingRecord();
    }

    /**
     * Create an instance of {@link AddressInterlocutor }
     * 
     */
    public AddressInterlocutor createAddressInterlocutor() {
        return new AddressInterlocutor();
    }

    /**
     * Create an instance of {@link VirtualTerminal.Functions }
     * 
     */
    public VirtualTerminal.Functions createVirtualTerminalFunctions() {
        return new VirtualTerminal.Functions();
    }

    /**
     * Create an instance of {@link BillingRecordList }
     * 
     */
    public BillingRecordList createBillingRecordList() {
        return new BillingRecordList();
    }

    /**
     * Create an instance of {@link OrderDetail }
     * 
     */
    public OrderDetail createOrderDetail() {
        return new OrderDetail();
    }

    /**
     * Create an instance of {@link PointOfSell }
     * 
     */
    public PointOfSell createPointOfSell() {
        return new PointOfSell();
    }

    /**
     * Create an instance of {@link BankAccount }
     * 
     */
    public BankAccount createBankAccount() {
        return new BankAccount();
    }

    /**
     * Create an instance of {@link VirtualTerminalFunction }
     * 
     */
    public VirtualTerminalFunction createVirtualTerminalFunction() {
        return new VirtualTerminalFunction();
    }

    /**
     * Create an instance of {@link TicketSend }
     * 
     */
    public TicketSend createTicketSend() {
        return new TicketSend();
    }

    /**
     * Create an instance of {@link ConnectionData }
     * 
     */
    public ConnectionData createConnectionData() {
        return new ConnectionData();
    }

    /**
     * Create an instance of {@link Subscription }
     * 
     */
    public Subscription createSubscription() {
        return new Subscription();
    }

    /**
     * Create an instance of {@link FailedObject }
     * 
     */
    public FailedObject createFailedObject() {
        return new FailedObject();
    }

    /**
     * Create an instance of {@link Rib }
     * 
     */
    public Rib createRib() {
        return new Rib();
    }

    /**
     * Create an instance of {@link PrivateDataList }
     * 
     */
    public PrivateDataList createPrivateDataList() {
        return new PrivateDataList();
    }

    /**
     * Create an instance of {@link Buyer }
     * 
     */
    public Buyer createBuyer() {
        return new Buyer();
    }

    /**
     * Create an instance of {@link Interlocutor }
     * 
     */
    public Interlocutor createInterlocutor() {
        return new Interlocutor();
    }

    /**
     * Create an instance of {@link VirtualTerminalFunction.FunctionParameter }
     * 
     */
    public VirtualTerminalFunction.FunctionParameter createVirtualTerminalFunctionFunctionParameter() {
        return new VirtualTerminalFunction.FunctionParameter();
    }

    /**
     * Create an instance of {@link TransactionList }
     * 
     */
    public TransactionList createTransactionList() {
        return new TransactionList();
    }

    /**
     * Create an instance of {@link Order }
     * 
     */
    public Order createOrder() {
        return new Order();
    }

    /**
     * Create an instance of {@link Iban }
     * 
     */
    public Iban createIban() {
        return new Iban();
    }

    /**
     * Create an instance of {@link Authorization }
     * 
     */
    public Authorization createAuthorization() {
        return new Authorization();
    }

    /**
     * Create an instance of {@link PrivateData }
     * 
     */
    public PrivateData createPrivateData() {
        return new PrivateData();
    }

    /**
     * Create an instance of {@link Recurring }
     * 
     */
    public Recurring createRecurring() {
        return new Recurring();
    }

    /**
     * Create an instance of {@link Owner }
     * 
     */
    public Owner createOwner() {
        return new Owner();
    }

    /**
     * Create an instance of {@link WalletIdList }
     * 
     */
    public WalletIdList createWalletIdList() {
        return new WalletIdList();
    }

    /**
     * Create an instance of {@link CaptureAuthorizationList }
     * 
     */
    public CaptureAuthorizationList createCaptureAuthorizationList() {
        return new CaptureAuthorizationList();
    }

    /**
     * Create an instance of {@link Option }
     * 
     */
    public Option createOption() {
        return new Option();
    }

    /**
     * Create an instance of {@link Refund }
     * 
     */
    public Refund createRefund() {
        return new Refund();
    }

    /**
     * Create an instance of {@link ResetAuthorizationList }
     * 
     */
    public ResetAuthorizationList createResetAuthorizationList() {
        return new ResetAuthorizationList();
    }

    /**
     * Create an instance of {@link Address }
     * 
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link RefundAuthorizationList }
     * 
     */
    public RefundAuthorizationList createRefundAuthorizationList() {
        return new RefundAuthorizationList();
    }

    /**
     * Create an instance of {@link TechnicalData }
     * 
     */
    public TechnicalData createTechnicalData() {
        return new TechnicalData();
    }

    /**
     * Create an instance of {@link Payment }
     * 
     */
    public Payment createPayment() {
        return new Payment();
    }

    /**
     * Create an instance of {@link PointOfSell.Contracts }
     * 
     */
    public PointOfSell.Contracts createPointOfSellContracts() {
        return new PointOfSell.Contracts();
    }

    /**
     * Create an instance of {@link Authentication3DSecure }
     * 
     */
    public Authentication3DSecure createAuthentication3DSecure() {
        return new Authentication3DSecure();
    }

    /**
     * Create an instance of {@link SelectedContractList }
     * 
     */
    public SelectedContractList createSelectedContractList() {
        return new SelectedContractList();
    }

    /**
     * Create an instance of {@link Card }
     * 
     */
    public Card createCard() {
        return new Card();
    }

    /**
     * Create an instance of {@link Capture }
     * 
     */
    public Capture createCapture() {
        return new Capture();
    }

    /**
     * Create an instance of {@link FailedListObject }
     * 
     */
    public FailedListObject createFailedListObject() {
        return new FailedListObject();
    }

    /**
     * Create an instance of {@link Wallet }
     * 
     */
    public Wallet createWallet() {
        return new Wallet();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://obj.ws.payline.experian.com", name = "label", scope = Contract.class)
    public JAXBElement<String> createContractLabel(String value) {
        return new JAXBElement<String>(_ContractLabel_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://obj.ws.payline.experian.com", name = "threeDSecure", scope = Transaction.class)
    public JAXBElement<String> createTransactionThreeDSecure(String value) {
        return new JAXBElement<String>(_TransactionThreeDSecure_QNAME, String.class, Transaction.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://obj.ws.payline.experian.com", name = "comments", scope = PointOfSell.class)
    public JAXBElement<String> createPointOfSellComments(String value) {
        return new JAXBElement<String>(_PointOfSellComments_QNAME, String.class, PointOfSell.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://obj.ws.payline.experian.com", name = "saleCondURL", scope = PointOfSell.class)
    public JAXBElement<String> createPointOfSellSaleCondURL(String value) {
        return new JAXBElement<String>(_PointOfSellSaleCondURL_QNAME, String.class, PointOfSell.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://obj.ws.payline.experian.com", name = "privateLifeURL", scope = PointOfSell.class)
    public JAXBElement<String> createPointOfSellPrivateLifeURL(String value) {
        return new JAXBElement<String>(_PointOfSellPrivateLifeURL_QNAME, String.class, PointOfSell.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://obj.ws.payline.experian.com", name = "endOfPaymentRedirection", scope = PointOfSell.class)
    public JAXBElement<Boolean> createPointOfSellEndOfPaymentRedirection(Boolean value) {
        return new JAXBElement<Boolean>(_PointOfSellEndOfPaymentRedirection_QNAME, Boolean.class, PointOfSell.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://obj.ws.payline.experian.com", name = "buyerMustAcceptSaleCond", scope = PointOfSell.class)
    public JAXBElement<Boolean> createPointOfSellBuyerMustAcceptSaleCond(Boolean value) {
        return new JAXBElement<Boolean>(_PointOfSellBuyerMustAcceptSaleCond_QNAME, Boolean.class, PointOfSell.class, value);
    }

}
