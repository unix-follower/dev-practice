import { Component, signal } from "@angular/core"
import type { FoodAdditiveSubstanceResponseDto } from "@/lib/features/chemistry/food-additives/model/FoodAdditiveResponse"
import ApiHttpClient, {
  ApiHttpClientSettings,
  ApiHttpClientType,
} from "@/lib/features/chemistry/food-additives/api-http-client"
import { ActivatedRoute } from "@angular/router"
import { environment } from "@/environments/environment"

@Component({
  selector: "app-food-additives",
  standalone: true,
  imports: [],
  templateUrl: "./PubChemFoodAdditives.html",
  styleUrl: "./PubChemFoodAdditives.css",
})
export default class FoodAdditives {
  apiHttpClient!: ApiHttpClient

  foodAdditiveList = signal<FoodAdditiveSubstanceResponseDto>([])

  constructor(private route: ActivatedRoute) {
    const mode = (this.route.snapshot.queryParams["mode"] as string) || null
    this.setupApiHttpClient(mode)
    this.apiHttpClient
      .getAllFoodAdditives(1, 10)
      .then((foodAdditives: FoodAdditiveSubstanceResponseDto) => {
        this.foodAdditiveList.update(() => foodAdditives)
      })
      .catch((reason) => console.error(reason))
  }

  private setupApiHttpClient(mode: string | null) {
    const settings: ApiHttpClientSettings =
      mode === "axios"
        ? {
            apiURL: environment.BACKEND_URL,
            clientStrategy: ApiHttpClientType.AXIOS,
          }
        : {
            apiURL: environment.BACKEND_URL,
            clientStrategy: ApiHttpClientType.FETCH,
          }
    this.apiHttpClient = new ApiHttpClient(settings)
  }
}
