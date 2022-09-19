//
//  CartScreen.swift
//  WCStoreApp
//
//  Created by Hicham on 16/9/2022.
//

import SwiftUI
import WCStoreAppKmm

class CartViewModelWrapper : ViewModelWrapper<CartViewModel> {
    @Published var uiState: CartViewModel.CartUiState!
    
    init() {
        super.init()
        uiState = viewModel.uiStateNativeValue
        assignToPublished(from: \.uiStateNative, to: &$uiState)
    }
}

struct CartScreen: View {
    @StateObject private var viewModelWrapper: CartViewModelWrapper = CartViewModelWrapper()
    
    private var viewModel: CartViewModel {
        return viewModelWrapper.viewModel
    }
    
    var body: some View {
        Screen() {
            let uiState = viewModelWrapper.uiState!
            if uiState.cartItems.isEmpty {
                Text("Cart is empty")
            } else {
                ZStack {
                VStack {
                    List {
                        ForEach(uiState.cartItems, id: \.product.id) { cartItem in
                            HStack {
                                if let image = cartItem.product.images.first,
                                   let url = URL(string: image) {
                                    AsyncImage(url: url) { image in
                                        image.resizable()
                                    } placeholder: {
                                        ProgressView()
                                    }
                                    .frame(width: 50, height: 50)
                                    .clipShape(RoundedRectangle(cornerRadius: 8))
                                } else {
                                    RoundedRectangle(cornerRadius: 8)
                                        .frame(width: 50, height: 50)
                                        .foregroundColor(.gray)
                                }
                                VStack(alignment: .leading, spacing: 2) {
                                    Text(cartItem.product.name )
                                        .font(.title3)
                                        .foregroundColor(.accentColor)
                                    Text(cartItem.totalPriceFormatted)
                                        .font(.footnote)
                                        .foregroundColor(.gray)
                                    CartStepper(
                                        count: Int(cartItem.quantity),
                                        addClick: {
                                            viewModel.onIncreaseQuantity(product: cartItem.product)
                                        },
                                        deleteClick: {
                                            viewModel.onDecreaseQuantity(product: cartItem.product)
                                        }
                                    )
                                }
                                
                                Spacer()
                                
                                Button(action: { viewModel.onRemoveProduct(product: cartItem.product) }) {
                                    Image(systemName: "trash.fill")
                                }
                            }.padding(4)
                        }
                    }.buttonStyle(.borderless)
                    
                    CartTotals(
                        subtotal: uiState.subtotalFormatted,
                        tax: uiState.taxFormatted,
                        total: uiState.totalFormatted,
                        buttonLabel: "Checkout",
                        onButtonClick: viewModel.onCheckoutClicked
                    )
                }
                }
                if (uiState.isUpdatingCart) {
                    ProgressView()
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                        .background(Color.gray.opacity(0.65))
                }
            }
        }.navigationTitle("Cart")
    }
}
