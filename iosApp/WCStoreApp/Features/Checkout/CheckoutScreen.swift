//
//  CheckoutScreen.swift
//  WCStoreApp
//
//  Created by Hicham on 16/9/2022.
//

import SwiftUI
import WCStoreAppKmm

class CheckoutViewModelProxy : ViewModelProxy<CheckoutViewModel> {
    @Published var uiState: CheckoutViewModel.UiState!
    
    @MainActor
    init() {
        super.init()
        uiState = viewModel.uiStateNativeValue
        assignToPublished(from: \.uiStateNative, to: &$uiState)
    }
}


struct CheckoutScreen: View {
    @StateObject var viewModelProxy = CheckoutViewModelProxy()
    
    private var viewModel: CheckoutViewModel {
        return viewModelProxy.viewModel
    }
    
    var body: some View {
        let uiState = viewModelProxy.uiState!
        Screen {
            ZStack {
                VStack {
                    ScrollView {
                        VStack {
                            VStack(alignment: .leading, spacing: 8) {
                                Text("Address").font(.title3).fontWeight(Font.Weight.bold).frame(maxWidth: .infinity, alignment: .leading)
                                Text(uiState.shippingAddress?.format() ?? "").frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                            }
                            .padding()
                            .frame(maxWidth: .infinity)
                            .background(Color.white)
                            .overlay(
                                RoundedRectangle(cornerRadius: 8)
                                    .stroke(.gray, lineWidth: 2)
                            )
                            .padding()
                            
                            VStack(alignment: .leading, spacing: 8) {
                                Text("Payment Method").font(.title3).fontWeight(Font.Weight.bold).frame(maxWidth: .infinity, alignment: .leading)
                                Text(uiState.selectedPaymentMethod?.title() ?? "None").frame(maxWidth: .infinity, alignment: .leading)
                            }
                            .padding()
                            .frame(maxWidth: .infinity)
                            .background(Color.white)
                            .overlay(
                                RoundedRectangle(cornerRadius: 8)
                                    .stroke(.gray, lineWidth: 2)
                            )
                            .padding()
                        }
                    }
                    
                    Spacer()
                    
                    CartTotals(
                        subtotal: uiState.subtotalFormatted,
                        tax: uiState.taxFormatted,
                        total: uiState.totalFormatted,
                        buttonLabel: "Place Order",
                        onButtonClick: viewModel.onPlacedOrderClicked
                    )
                }.background(Color(.systemGroupedBackground))
                
                if uiState.isLoading {
                    ProgressView()
                        .frame(width: 60, height: 60, alignment: .center)
                        .background(Color.white)
                        .cornerRadius(8)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                        .background(Color(.lightGray).opacity(0.5))
                }
            }
        }.effects(viewModel: viewModel)
            .navigationTitle("Checkout")
    }
}


extension PaymentMethod {
    func title() -> String {
        switch (self) {
        case is PaymentMethod.WIRE: return "Direct Bank Transfer (BACS)"
        case is PaymentMethod.CASH: return "Cash on Delivery"
        case is PaymentMethod.WCPayCard: return "Card ending in ${card.number.takeLast(4)} (expires ${card.expiryMonth}/${card.expiryYear})"
        default: fatalError()
        }
    }
}
