//
//  ProductScreen.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 13/3/2022.
//

import SwiftUI
import WCStoreAppKmm
import KMPNativeCoroutinesCombine

class ProductViewModelProxy : ViewModelProxy<ProductViewModel> {
    @Published var uiState: ProductViewModel.UiState!
    
    init(productId: Int) {
        super.init(parameters: [productId])
        uiState = viewModel.uiStateNativeValue
        assignToPublished(from: \.uiStateNative, to: &$uiState)
    }
}

struct ProductScreen: View {
    @StateObject var viewModelProxy: ProductViewModelProxy
    
    private var viewModel: ProductViewModel {
        return viewModelProxy.viewModel
    }
    
    var body: some View {
        Screen() {
            switch(viewModelProxy.uiState) {
            case is ProductViewModel.UiStateLoadingState:
                ProgressView()
            case let successState as ProductViewModel.UiStateSuccessState:
                let product = successState.product
                VStack(alignment: .leading) {
                    if let image = product.images.first,
                       let url = URL(string: image) {
                        AsyncImage(url: url) { image in
                            image.resizable()
                        } placeholder: {
                            ProgressView()
                        }
                        .aspectRatio(1, contentMode: ContentMode.fit)
                        .frame(maxWidth: .infinity)
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                        .padding()
                    } else {
                        RoundedRectangle(cornerRadius: 8)
                            .frame(maxWidth: .infinity)
                            .foregroundColor(.gray)
                            .padding()
                    }
                    Text(product.name)
                        .font(.system(.headline))
                        .padding(.horizontal)
                    Text(product.shortDescription)
                        .padding(.horizontal)
                    Spacer()
                    Button(action: { viewModel.onAddToCartClicked() }) {
                        Text("Add to Cart")
                    }
                    .frame(maxWidth:.infinity, alignment: .center)
                    .padding()
                }.frame(maxWidth:.infinity, maxHeight: .infinity)
            default:
                EmptyView()
            }
        }
        .effects(viewModel: viewModel)
    }
}
