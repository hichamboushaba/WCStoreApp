//
//  ProductScreen.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 13/3/2022.
//

import SwiftUI
import WCStoreAppKmm
import KMPNativeCoroutinesCombine

struct ProductScreen: View {
    private let viewModel: ProductViewModel
    @FlowWrapper private var uiState: ProductViewModel.UiState
    
    init(productId: Int64) {
        viewModel = KoinKt.get(objCClass: ProductViewModel.self, parameters: [productId]) as! ProductViewModel
        _uiState = FlowWrapper(viewModel.uiStateNative, initialValue: viewModel.uiStateNativeValue)
    }
    
    var body: some View {
        Screen() {
            switch(uiState) {
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

struct ProductScreen_Previews: PreviewProvider {
    static var previews: some View {
        ProductScreen(productId: 0)
    }
}
